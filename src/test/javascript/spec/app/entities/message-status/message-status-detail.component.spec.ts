/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { MessageStatusDetailComponent } from '../../../../../../main/webapp/app/entities/message-status/message-status-detail.component';
import { MessageStatusService } from '../../../../../../main/webapp/app/entities/message-status/message-status.service';
import { MessageStatus } from '../../../../../../main/webapp/app/entities/message-status/message-status.model';

describe('Component Tests', () => {

    describe('MessageStatus Management Detail Component', () => {
        let comp: MessageStatusDetailComponent;
        let fixture: ComponentFixture<MessageStatusDetailComponent>;
        let service: MessageStatusService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [MessageStatusDetailComponent],
                providers: [
                    MessageStatusService
                ]
            })
            .overrideTemplate(MessageStatusDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessageStatusDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessageStatusService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MessageStatus(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.messageStatus).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
