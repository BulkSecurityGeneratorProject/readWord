import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Audio } from './audio.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Audio>;

@Injectable()
export class AudioService {

    private resourceUrl =  SERVER_API_URL + 'api/audio';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/audio';

    constructor(private http: HttpClient) { }

    create(audio: Audio): Observable<EntityResponseType> {
        const copy = this.convert(audio);
        return this.http.post<Audio>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(audio: Audio): Observable<EntityResponseType> {
        const copy = this.convert(audio);
        return this.http.put<Audio>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Audio>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Audio[]>> {
        const options = createRequestOption(req);
        return this.http.get<Audio[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Audio[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Audio[]>> {
        const options = createRequestOption(req);
        return this.http.get<Audio[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Audio[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Audio = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Audio[]>): HttpResponse<Audio[]> {
        const jsonResponse: Audio[] = res.body;
        const body: Audio[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Audio.
     */
    private convertItemFromServer(audio: Audio): Audio {
        const copy: Audio = Object.assign({}, audio);
        return copy;
    }

    /**
     * Convert a Audio to a JSON which can be sent to the server.
     */
    private convert(audio: Audio): Audio {
        const copy: Audio = Object.assign({}, audio);
        return copy;
    }
}
