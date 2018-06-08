/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReadWordTestModule } from '../../../test.module';
import { VipOrderComponent } from '../../../../../../main/webapp/app/entities/vip-order/vip-order.component';
import { VipOrderService } from '../../../../../../main/webapp/app/entities/vip-order/vip-order.service';
import { VipOrder } from '../../../../../../main/webapp/app/entities/vip-order/vip-order.model';

describe('Component Tests', () => {

    describe('VipOrder Management Component', () => {
        let comp: VipOrderComponent;
        let fixture: ComponentFixture<VipOrderComponent>;
        let service: VipOrderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [VipOrderComponent],
                providers: [
                    VipOrderService
                ]
            })
            .overrideTemplate(VipOrderComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VipOrderComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VipOrderService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new VipOrder(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.vipOrders[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
