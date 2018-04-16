/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { MessageContentDetailComponent } from '../../../../../../main/webapp/app/entities/message-content/message-content-detail.component';
import { MessageContentService } from '../../../../../../main/webapp/app/entities/message-content/message-content.service';
import { MessageContent } from '../../../../../../main/webapp/app/entities/message-content/message-content.model';

describe('Component Tests', () => {

    describe('MessageContent Management Detail Component', () => {
        let comp: MessageContentDetailComponent;
        let fixture: ComponentFixture<MessageContentDetailComponent>;
        let service: MessageContentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [MessageContentDetailComponent],
                providers: [
                    MessageContentService
                ]
            })
            .overrideTemplate(MessageContentDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessageContentDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessageContentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new MessageContent(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.messageContent).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
