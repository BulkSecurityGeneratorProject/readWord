/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { SlideDetailComponent } from '../../../../../../main/webapp/app/entities/slide/slide-detail.component';
import { SlideService } from '../../../../../../main/webapp/app/entities/slide/slide.service';
import { Slide } from '../../../../../../main/webapp/app/entities/slide/slide.model';

describe('Component Tests', () => {

    describe('Slide Management Detail Component', () => {
        let comp: SlideDetailComponent;
        let fixture: ComponentFixture<SlideDetailComponent>;
        let service: SlideService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [SlideDetailComponent],
                providers: [
                    SlideService
                ]
            })
            .overrideTemplate(SlideDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SlideDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SlideService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Slide(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.slide).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
