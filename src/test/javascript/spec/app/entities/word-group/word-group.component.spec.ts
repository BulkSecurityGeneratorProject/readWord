/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { WordGroupComponent } from '../../../../../../main/webapp/app/entities/word-group/word-group.component';
import { WordGroupService } from '../../../../../../main/webapp/app/entities/word-group/word-group.service';
import { WordGroup } from '../../../../../../main/webapp/app/entities/word-group/word-group.model';

describe('Component Tests', () => {

    describe('WordGroup Management Component', () => {
        let comp: WordGroupComponent;
        let fixture: ComponentFixture<WordGroupComponent>;
        let service: WordGroupService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [WordGroupComponent],
                providers: [
                    WordGroupService
                ]
            })
            .overrideTemplate(WordGroupComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WordGroupComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WordGroupService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new WordGroup(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.wordGroups[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
