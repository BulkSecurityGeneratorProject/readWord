/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { FavoriteDetailComponent } from '../../../../../../main/webapp/app/entities/favorite/favorite-detail.component';
import { FavoriteService } from '../../../../../../main/webapp/app/entities/favorite/favorite.service';
import { Favorite } from '../../../../../../main/webapp/app/entities/favorite/favorite.model';

describe('Component Tests', () => {

    describe('Favorite Management Detail Component', () => {
        let comp: FavoriteDetailComponent;
        let fixture: ComponentFixture<FavoriteDetailComponent>;
        let service: FavoriteService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [FavoriteDetailComponent],
                providers: [
                    FavoriteService
                ]
            })
            .overrideTemplate(FavoriteDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FavoriteDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FavoriteService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Favorite(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.favorite).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
