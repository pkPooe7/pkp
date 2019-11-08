import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IAlien } from 'app/shared/model/alien.model';

type EntityResponseType = HttpResponse<IAlien>;
type EntityArrayResponseType = HttpResponse<IAlien[]>;

@Injectable({ providedIn: 'root' })
export class AlienService {
  public resourceUrl = SERVER_API_URL + 'api/aliens';

  constructor(protected http: HttpClient) {}

  create(alien: IAlien): Observable<EntityResponseType> {
    return this.http.post<IAlien>(this.resourceUrl, alien, { observe: 'response' });
  }

  update(alien: IAlien): Observable<EntityResponseType> {
    return this.http.put<IAlien>(this.resourceUrl, alien, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAlien>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlien[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
