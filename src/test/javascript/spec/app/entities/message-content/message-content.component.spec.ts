/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { MessageContentComponent } from '../../../../../../main/webapp/app/entities/message-content/message-content.component';
import { MessageContentService } from '../../../../../../main/webapp/app/entities/message-content/message-content.service';
import { MessageContent } from '../../../../../../main/webapp/app/entities/message-content/message-content.model';

describe('Component Tests', () => {

    describe('MessageContent Management Component', () => {
        let comp: MessageContentComponent;
        let fixture: ComponentFixture<MessageContentComponent>;
        let service: MessageContentService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [MessageContentComponent],
                providers: [
                    MessageContentService
                ]
            })
            .overrideTemplate(MessageContentComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MessageContentComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MessageContentService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new MessageContent(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.messageContents[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
