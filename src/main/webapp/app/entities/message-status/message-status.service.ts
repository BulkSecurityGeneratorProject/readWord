import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MessageStatus } from './message-status.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MessageStatus>;

@Injectable()
export class MessageStatusService {

    private resourceUrl =  SERVER_API_URL + 'api/message-statuses';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/message-statuses';

    constructor(private http: HttpClient) { }

    create(messageStatus: MessageStatus): Observable<EntityResponseType> {
        const copy = this.convert(messageStatus);
        return this.http.post<MessageStatus>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(messageStatus: MessageStatus): Observable<EntityResponseType> {
        const copy = this.convert(messageStatus);
        return this.http.put<MessageStatus>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MessageStatus>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MessageStatus[]>> {
        const options = createRequestOption(req);
        return this.http.get<MessageStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MessageStatus[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<MessageStatus[]>> {
        const options = createRequestOption(req);
        return this.http.get<MessageStatus[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MessageStatus[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MessageStatus = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MessageStatus[]>): HttpResponse<MessageStatus[]> {
        const jsonResponse: MessageStatus[] = res.body;
        const body: MessageStatus[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MessageStatus.
     */
    private convertItemFromServer(messageStatus: MessageStatus): MessageStatus {
        const copy: MessageStatus = Object.assign({}, messageStatus);
        return copy;
    }

    /**
     * Convert a MessageStatus to a JSON which can be sent to the server.
     */
    private convert(messageStatus: MessageStatus): MessageStatus {
        const copy: MessageStatus = Object.assign({}, messageStatus);
        return copy;
    }
}
