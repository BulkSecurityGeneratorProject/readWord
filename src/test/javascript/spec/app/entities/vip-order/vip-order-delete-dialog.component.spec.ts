/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ReadWordTestModule } from '../../../test.module';
import { VipOrderDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/vip-order/vip-order-delete-dialog.component';
import { VipOrderService } from '../../../../../../main/webapp/app/entities/vip-order/vip-order.service';

describe('Component Tests', () => {

    describe('VipOrder Management Delete Component', () => {
        let comp: VipOrderDeleteDialogComponent;
        let fixture: ComponentFixture<VipOrderDeleteDialogComponent>;
        let service: VipOrderService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ReadWordTestModule],
                declarations: [VipOrderDeleteDialogComponent],
                providers: [
                    VipOrderService
                ]
            })
            .overrideTemplate(VipOrderDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VipOrderDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VipOrderService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
