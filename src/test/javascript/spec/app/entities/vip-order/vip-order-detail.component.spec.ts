/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ReadWordTestModule } from '../../../test.module';
import { VipOrderDetailComponent } from '../../../../../../main/webapp/app/entities/vip-order/vip-order-detail.component';
import { VipOrderService } from '../../../../../../main/webapp/app/entities/vip-order/vip-order.service';
import { VipOrder } from '../../../../../../main/webapp/app/entities/vip-order/vip-order.model';

describe('Component Tests', () => {

    describe('VipOrder Management Detail Component', () => {
        let comp: VipOrderDetailComponent;
        let fixture: ComponentFixture<VipOrderDetailComponent>;
        let service: VipOrderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [VipOrderDetailComponent],
                providers: [
                    VipOrderService
                ]
            })
            .overrideTemplate(VipOrderDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VipOrderDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VipOrderService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new VipOrder(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.vipOrder).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
