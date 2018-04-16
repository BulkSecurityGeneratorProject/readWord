import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Word } from './word.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Word>;

@Injectable()
export class WordService {

    private resourceUrl =  SERVER_API_URL + 'api/words';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/words';

    constructor(private http: HttpClient) { }

    create(word: Word): Observable<EntityResponseType> {
        const copy = this.convert(word);
        return this.http.post<Word>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(word: Word): Observable<EntityResponseType> {
        const copy = this.convert(word);
        return this.http.put<Word>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Word>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Word[]>> {
        const options = createRequestOption(req);
        return this.http.get<Word[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Word[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Word[]>> {
        const options = createRequestOption(req);
        return this.http.get<Word[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Word[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Word = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Word[]>): HttpResponse<Word[]> {
        const jsonResponse: Word[] = res.body;
        const body: Word[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Word.
     */
    private convertItemFromServer(word: Word): Word {
        const copy: Word = Object.assign({}, word);
        return copy;
    }

    /**
     * Convert a Word to a JSON which can be sent to the server.
     */
    private convert(word: Word): Word {
        const copy: Word = Object.assign({}, word);
        return copy;
    }
}
