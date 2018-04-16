/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { AudioComponent } from '../../../../../../main/webapp/app/entities/audio/audio.component';
import { AudioService } from '../../../../../../main/webapp/app/entities/audio/audio.service';
import { Audio } from '../../../../../../main/webapp/app/entities/audio/audio.model';

describe('Component Tests', () => {

    describe('Audio Management Component', () => {
        let comp: AudioComponent;
        let fixture: ComponentFixture<AudioComponent>;
        let service: AudioService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [AudioComponent],
                providers: [
                    AudioService
                ]
            })
            .overrideTemplate(AudioComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AudioComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AudioService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Audio(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.audio[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
