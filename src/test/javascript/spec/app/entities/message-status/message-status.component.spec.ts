/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { MessageStatusComponent } from '../../../../../../main/webapp/app/entities/message-status/message-status.component';
import { MessageStatusService } from '../../../../../../main/webapp/app/entities/message-status/message-status.service';
import { MessageStatus } from '../../../../../../main/webapp/app/entities/message-status/message-status.model';

describe('Component Tests', () => {

    describe('MessageStatus Management Component', () => {
        let comp: MessageStatusComponent;
        let fixture: ComponentFixture<MessageStatusComponent>;
        let service: MessageStatusService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [MessageStatusComponent],
                providers: [
                    MessageStatusService
                ]
            })
            .overrideTemplate(MessageStatusComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessageStatusComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessageStatusService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MessageStatus(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.messageStatuses[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
