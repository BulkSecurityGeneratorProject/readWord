import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { VipOrder } from './vip-order.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<VipOrder>;

@Injectable()
export class VipOrderService {

    private resourceUrl =  SERVER_API_URL + 'api/vip-orders';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/vip-orders';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(vipOrder: VipOrder): Observable<EntityResponseType> {
        const copy = this.convert(vipOrder);
        return this.http.post<VipOrder>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(vipOrder: VipOrder): Observable<EntityResponseType> {
        const copy = this.convert(vipOrder);
        return this.http.put<VipOrder>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<VipOrder>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<VipOrder[]>> {
        const options = createRequestOption(req);
        return this.http.get<VipOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<VipOrder[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<VipOrder[]>> {
        const options = createRequestOption(req);
        return this.http.get<VipOrder[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<VipOrder[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: VipOrder = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<VipOrder[]>): HttpResponse<VipOrder[]> {
        const jsonResponse: VipOrder[] = res.body;
        const body: VipOrder[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to VipOrder.
     */
    private convertItemFromServer(vipOrder: VipOrder): VipOrder {
        const copy: VipOrder = Object.assign({}, vipOrder);
        copy.createTime = this.dateUtils
            .convertDateTimeFromServer(vipOrder.createTime);
        copy.paymentTime = this.dateUtils
            .convertDateTimeFromServer(vipOrder.paymentTime);
        return copy;
    }

    /**
     * Convert a VipOrder to a JSON which can be sent to the server.
     */
    private convert(vipOrder: VipOrder): VipOrder {
        const copy: VipOrder = Object.assign({}, vipOrder);

        copy.createTime = this.dateUtils.toDate(vipOrder.createTime);

        copy.paymentTime = this.dateUtils.toDate(vipOrder.paymentTime);
        return copy;
    }
}
