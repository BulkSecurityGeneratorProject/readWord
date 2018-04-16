import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { MessageContent } from './message-content.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<MessageContent>;

@Injectable()
export class MessageContentService {

    private resourceUrl =  SERVER_API_URL + 'api/message-contents';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/message-contents';

    constructor(private http: HttpClient) { }

    create(messageContent: MessageContent): Observable<EntityResponseType> {
        const copy = this.convert(messageContent);
        return this.http.post<MessageContent>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(messageContent: MessageContent): Observable<EntityResponseType> {
        const copy = this.convert(messageContent);
        return this.http.put<MessageContent>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<MessageContent>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<MessageContent[]>> {
        const options = createRequestOption(req);
        return this.http.get<MessageContent[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MessageContent[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<MessageContent[]>> {
        const options = createRequestOption(req);
        return this.http.get<MessageContent[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<MessageContent[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: MessageContent = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<MessageContent[]>): HttpResponse<MessageContent[]> {
        const jsonResponse: MessageContent[] = res.body;
        const body: MessageContent[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to MessageContent.
     */
    private convertItemFromServer(messageContent: MessageContent): MessageContent {
        const copy: MessageContent = Object.assign({}, messageContent);
        return copy;
    }

    /**
     * Convert a MessageContent to a JSON which can be sent to the server.
     */
    private convert(messageContent: MessageContent): MessageContent {
        const copy: MessageContent = Object.assign({}, messageContent);
        return copy;
    }
}
