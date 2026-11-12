import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';
import { EmergencyService } from './core/services/emergency.service';
import { IncidentType, Incident } from './shared/models/incident.model';
import { LandingComponent } from './modules/public/landing/landing.component';
import { IncidentReportModalComponent } from './shared/components/incident-modal/incident-modal.component';
import { LocationService } from './core/services/location.service';
import { FormBuilder } from '@angular/forms';
import { Subject, of, throwError } from 'rxjs';
import { API_ORIGIN } from './core/api.config';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { HttpRequest, HttpResponse } from '@angular/common/http';
import { IncidentHistoryComponent } from './modules/responder/past-incidents/past-incidents.component';
import { ProfileHeaderComponent } from './modules/responder/profile-card/profile-card.component';
import { ResponderFormComponent } from './modules/admin/responder-management/responder-form/responder-form.component';

const incident: Incident = {incidentId: 0, type: IncidentType.SOS_REQUEST, status: 'NEW',
  victimName: '', victimContact: '', responderIds: [],
  incidentLocation: {latitude: 12, longitude: 77}, victimLocation: {latitude: 12, longitude: 77}};

describe('Stabilization: HTTP acknowledgement', () => {
  let service: EmergencyService;
  let http: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({providers: [provideHttpClient(), provideHttpClientTesting(),
      {provide: MatDialog, useValue: {open: jasmine.createSpy()}}]});
    service = TestBed.inject(EmergencyService); http = TestBed.inject(HttpTestingController);
  });
  afterEach(() => http.verify());
  it('is cold and emits exactly one durable reference without victim details', () => {
    const result = service.reportIncident(incident); const next = jasmine.createSpy();
    http.expectNone(`${API_ORIGIN}/incidents/create`);
    result.subscribe(next);
    const request = http.expectOne(`${API_ORIGIN}/incidents/create`);
    expect(request.request.body.victimName).toBeNull(); expect(request.request.body.type).toBe('SOS_REQUEST');
    request.flush({...incident, incidentId: 42}, {status: 201, statusText: 'Created'});
    expect(next).toHaveBeenCalledTimes(1); expect(service.getcurrentIncidentId()).toBe(42);
  });
  it('propagates server failures without success', () => {
    const next = jasmine.createSpy(), error = jasmine.createSpy(); service.reportIncident(incident).subscribe({next,error});
    http.expectOne(`${API_ORIGIN}/incidents/create`).flush({}, {status: 500,statusText:'Failure'});
    expect(next).not.toHaveBeenCalled(); expect(error).toHaveBeenCalledTimes(1);
  });
  it('rejects a success body without a server reference', () => {
    const error = jasmine.createSpy(); service.reportIncident(incident).subscribe({error});
    http.expectOne(`${API_ORIGIN}/incidents/create`).flush(incident);
    expect(error).toHaveBeenCalled();
  });
  it('times out as unconfirmed', fakeAsync(() => {
    const error = jasmine.createSpy(); service.reportIncident(incident).subscribe({error});
    const request = http.expectOne(`${API_ORIGIN}/incidents/create`); tick(15001);
    expect(error).toHaveBeenCalled(); expect(request.cancelled).toBeTrue();
  }));
  it('maps Accident to TRAFFIC_ACCIDENT', () => {
    service.reportIncident({...incident,type:IncidentType.ACCIDENT}).subscribe();
    const request = http.expectOne(`${API_ORIGIN}/incidents/create`);
    expect(request.request.body.type).toBe('TRAFFIC_ACCIDENT'); request.flush({...incident,incidentId:1});
  });
});

describe('Stabilization: truthful SOS feedback', () => {
  let component: LandingComponent, dialog: any, service: any, location: any, response: Subject<Incident>;
  beforeEach(() => {
    response = new Subject(); dialog = {open:jasmine.createSpy()};
    service = {reportIncident:jasmine.createSpy().and.returnValue(response)};
    location = {getCurrentLocation: jasmine.createSpy().and.resolveTo({latitude:12,longitude:77})};
    component = new LandingComponent(dialog,location,service,{} as any,{} as any);
  });
  it('waits for receipt and prevents duplicate taps', async () => {
    await component.sendSOSAlert(); await component.sendSOSAlert();
    expect(dialog.open).not.toHaveBeenCalled(); expect(service.reportIncident).toHaveBeenCalledTimes(1);
    response.next({...incident,incidentId:42});
    expect(dialog.open.calls.mostRecent().args[1].data.incident.incidentId).toBe(42);
  });
  it('shows unconfirmed on network failure', async () => {
    await component.sendSOSAlert(); response.error(new Error());
    expect(dialog.open).not.toHaveBeenCalled(); expect(component.submissionError).toContain('not confirmed');
  });
  it('does not submit invented coordinates on GPS denial', async () => {
    location.getCurrentLocation.and.rejectWith(new Error()); await component.sendSOSAlert();
    expect(service.reportIncident).not.toHaveBeenCalled(); expect(component.submissionError).toContain('no report sent');
  });
});

describe('Stabilization: incident form', () => {
  it('keeps failures visible and confirms only server data', () => {
    const response = new Subject<Incident>();
    const service = {reportIncident:jasmine.createSpy().and.returnValue(response),setFormData:jasmine.createSpy(),openIncidentConfirmationModal:jasmine.createSpy()};
    const ref = {close:jasmine.createSpy()};
    const component = new IncidentReportModalComponent(new FormBuilder(),{} as any,service as any,ref as any);
    component.initForm(); component.userLocation={lat:12,lng:77}; component.incidentForm.patchValue({type:IncidentType.FIRE});
    component.onSubmit(); expect(ref.close).not.toHaveBeenCalled();
    response.error(new Error()); expect(service.openIncidentConfirmationModal).not.toHaveBeenCalled();
    expect(component.submissionError).toContain('not confirmed');
  });
  it('refuses to substitute reporter GPS for a different incident location', () => {
    const service = {reportIncident:jasmine.createSpy()};
    const component = new IncidentReportModalComponent(new FormBuilder(),{} as any,service as any,{} as any);
    component.initForm(); component.userLocation={lat:12,lng:77};
    component.incidentForm.patchValue({type:IncidentType.FIRE,useSameLocation:false}); component.onSubmit();
    expect(service.reportIncident).not.toHaveBeenCalled(); expect(component.submissionError).toContain('location is not confirmed');
  });
});

describe('Stabilization: credentials', () => {
  it('valid GPS does not depend on geocoding or Google Maps', async () => {
    const service = new LocationService({} as any);
    const geocode = spyOn(service,'getAddressFromCoordinates').and.rejectWith(new Error('unavailable'));
    spyOn(navigator.geolocation,'getCurrentPosition').and.callFake(success => success({coords:{latitude:12,longitude:77,accuracy:8}} as any));
    const location = await service.getCurrentLocation();
    expect(location.latitude).toBe(12); expect(location.longitude).toBe(77); expect(geocode).not.toHaveBeenCalled();
  });
  it('uses shared auth storage only for private same-origin requests', () => {
    const interceptor = new AuthInterceptor({getToken:()=> 'test-token'} as any);
    const next = {handle:jasmine.createSpy().and.returnValue(of(new HttpResponse()))};
    for (const url of [`${API_ORIGIN}/incidents/create`, 'https://outside.example/data']) {
      interceptor.intercept(new HttpRequest('POST',url,{}),next);
      expect(next.handle.calls.mostRecent().args[0].headers.has('Authorization')).toBeFalse();
    }
    interceptor.intercept(new HttpRequest('GET',`${API_ORIGIN}/responders/1`),next);
    expect(next.handle.calls.mostRecent().args[0].headers.get('Authorization')).toBe('Bearer test-token');
  });
});

describe('Stabilization: responder history', () => {
  it('admin profile editing preserves an existing assignment status', () => {
    const existing = {responderId:7,status:'ASSIGNED',incidentId:42,name:'staff'} as any;
    const service = {updateResponder:jasmine.createSpy().and.returnValue(of(existing))};
    const component = new ResponderFormComponent({close:()=>{}} as any,service as any);
    component.responder=existing; component.ngOnInit(); component.handleSubmit();
    expect(service.updateResponder.calls.mostRecent().args[1].status).toBe('ASSIGNED');
  });
  it('profile waits for persisted status and preserves previous status on failure', () => {
    const response = new Subject<any>(); const service = {updateStatus:jasmine.createSpy().and.returnValue(response)};
    const component = new ProfileHeaderComponent(service as any,{} as any,{} as any);
    component.responder = {responderId:7,status:'AVAILABLE'} as any; component.status='AVAILABLE';
    component.setStatus('OFF_DUTY'); expect(component.status).toBe('AVAILABLE');
    response.error(new Error()); expect(component.status).toBe('AVAILABLE'); expect(component.statusError).toContain('not confirmed');
  });
  afterEach(() => sessionStorage.removeItem('responderId'));
  it('loads real events for signed-in responder', () => {
    sessionStorage.setItem('responderId','7'); const events = [{incidentId:42,eventType:'ASSIGNED'}];
    const http = {get:jasmine.createSpy().and.returnValue(of(events))};
    const component = new IncidentHistoryComponent(http as any); component.ngOnInit();
    expect(http.get).toHaveBeenCalledWith(`${API_ORIGIN}/responders/7/history`); expect(component.events.length).toBe(1);
  });
  it('shows unavailable instead of fabricated history', () => {
    sessionStorage.setItem('responderId','7');
    const component = new IncidentHistoryComponent({get:()=>throwError(()=>new Error())} as any);
    component.ngOnInit(); expect(component.events).toEqual([]); expect(component.message).toContain('could not');
  });
});
