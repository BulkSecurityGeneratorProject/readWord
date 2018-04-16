/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { FavoriteComponent } from '../../../../../../main/webapp/app/entities/favorite/favorite.component';
import { FavoriteService } from '../../../../../../main/webapp/app/entities/favorite/favorite.service';
import { Favorite } from '../../../../../../main/webapp/app/entities/favorite/favorite.model';

describe('Component Tests', () => {

    describe('Favorite Management Component', () => {
        let comp: FavoriteComponent;
        let fixture: ComponentFixture<FavoriteComponent>;
        let service: FavoriteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [FavoriteComponent],
                providers: [
                    FavoriteService
                ]
            })
            .overrideTemplate(FavoriteComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FavoriteComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FavoriteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Favorite(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.favorites[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
