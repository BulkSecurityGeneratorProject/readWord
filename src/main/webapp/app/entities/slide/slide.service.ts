import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Slide } from './slide.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Slide>;

@Injectable()
export class SlideService {

    private resourceUrl =  SERVER_API_URL + 'api/slides';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/slides';

    constructor(private http: HttpClient) { }

    create(slide: Slide): Observable<EntityResponseType> {
        const copy = this.convert(slide);
        return this.http.post<Slide>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(slide: Slide): Observable<EntityResponseType> {
        const copy = this.convert(slide);
        return this.http.put<Slide>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Slide>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Slide[]>> {
        const options = createRequestOption(req);
        return this.http.get<Slide[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Slide[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Slide[]>> {
        const options = createRequestOption(req);
        return this.http.get<Slide[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Slide[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Slide = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Slide[]>): HttpResponse<Slide[]> {
        const jsonResponse: Slide[] = res.body;
        const body: Slide[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Slide.
     */
    private convertItemFromServer(slide: Slide): Slide {
        const copy: Slide = Object.assign({}, slide);
        return copy;
    }

    /**
     * Convert a Slide to a JSON which can be sent to the server.
     */
    private convert(slide: Slide): Slide {
        const copy: Slide = Object.assign({}, slide);
        return copy;
    }
}
