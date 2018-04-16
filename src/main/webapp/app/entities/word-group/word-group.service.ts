import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { WordGroup } from './word-group.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<WordGroup>;

@Injectable()
export class WordGroupService {

    private resourceUrl =  SERVER_API_URL + 'api/word-groups';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/word-groups';

    constructor(private http: HttpClient) { }

    create(wordGroup: WordGroup): Observable<EntityResponseType> {
        const copy = this.convert(wordGroup);
        return this.http.post<WordGroup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(wordGroup: WordGroup): Observable<EntityResponseType> {
        const copy = this.convert(wordGroup);
        return this.http.put<WordGroup>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<WordGroup>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<WordGroup[]>> {
        const options = createRequestOption(req);
        return this.http.get<WordGroup[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<WordGroup[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<WordGroup[]>> {
        const options = createRequestOption(req);
        return this.http.get<WordGroup[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<WordGroup[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: WordGroup = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<WordGroup[]>): HttpResponse<WordGroup[]> {
        const jsonResponse: WordGroup[] = res.body;
        const body: WordGroup[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to WordGroup.
     */
    private convertItemFromServer(wordGroup: WordGroup): WordGroup {
        const copy: WordGroup = Object.assign({}, wordGroup);
        return copy;
    }

    /**
     * Convert a WordGroup to a JSON which can be sent to the server.
     */
    private convert(wordGroup: WordGroup): WordGroup {
        const copy: WordGroup = Object.assign({}, wordGroup);
        return copy;
    }
}
