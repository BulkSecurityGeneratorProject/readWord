import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Favorite } from './favorite.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Favorite>;

@Injectable()
export class FavoriteService {

    private resourceUrl =  SERVER_API_URL + 'api/favorites';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/favorites';

    constructor(private http: HttpClient) { }

    create(favorite: Favorite): Observable<EntityResponseType> {
        const copy = this.convert(favorite);
        return this.http.post<Favorite>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(favorite: Favorite): Observable<EntityResponseType> {
        const copy = this.convert(favorite);
        return this.http.put<Favorite>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Favorite>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Favorite[]>> {
        const options = createRequestOption(req);
        return this.http.get<Favorite[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Favorite[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Favorite[]>> {
        const options = createRequestOption(req);
        return this.http.get<Favorite[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Favorite[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Favorite = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Favorite[]>): HttpResponse<Favorite[]> {
        const jsonResponse: Favorite[] = res.body;
        const body: Favorite[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Favorite.
     */
    private convertItemFromServer(favorite: Favorite): Favorite {
        const copy: Favorite = Object.assign({}, favorite);
        return copy;
    }

    /**
     * Convert a Favorite to a JSON which can be sent to the server.
     */
    private convert(favorite: Favorite): Favorite {
        const copy: Favorite = Object.assign({}, favorite);
        return copy;
    }
}
