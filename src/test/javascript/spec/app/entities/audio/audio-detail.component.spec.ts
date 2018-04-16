/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { AudioDetailComponent } from '../../../../../../main/webapp/app/entities/audio/audio-detail.component';
import { AudioService } from '../../../../../../main/webapp/app/entities/audio/audio.service';
import { Audio } from '../../../../../../main/webapp/app/entities/audio/audio.model';

describe('Component Tests', () => {

    describe('Audio Management Detail Component', () => {
        let comp: AudioDetailComponent;
        let fixture: ComponentFixture<AudioDetailComponent>;
        let service: AudioService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [AudioDetailComponent],
                providers: [
                    AudioService
                ]
            })
            .overrideTemplate(AudioDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AudioDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AudioService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Audio(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.audio).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
