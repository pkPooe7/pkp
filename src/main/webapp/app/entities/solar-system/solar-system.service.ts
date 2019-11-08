import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ISolarSystem } from 'app/shared/model/solar-system.model';

type EntityResponseType = HttpResponse<ISolarSystem>;
type EntityArrayResponseType = HttpResponse<ISolarSystem[]>;

@Injectable({ providedIn: 'root' })
export class SolarSystemService {
  public resourceUrl = SERVER_API_URL + 'api/solar-systems';

  constructor(protected http: HttpClient) {}

  create(solarSystem: ISolarSystem): Observable<EntityResponseType> {
    return this.http.post<ISolarSystem>(this.resourceUrl, solarSystem, { observe: 'response' });
  }

  update(solarSystem: ISolarSystem): Observable<EntityResponseType> {
    return this.http.put<ISolarSystem>(this.resourceUrl, solarSystem, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISolarSystem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISolarSystem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
