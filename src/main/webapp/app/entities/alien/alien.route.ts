import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Alien } from 'app/shared/model/alien.model';
import { AlienService } from './alien.service';
import { AlienComponent } from './alien.component';
import { AlienDetailComponent } from './alien-detail.component';
import { AlienUpdateComponent } from './alien-update.component';
import { AlienDeletePopupComponent } from './alien-delete-dialog.component';
import { IAlien } from 'app/shared/model/alien.model';

@Injectable({ providedIn: 'root' })
export class AlienResolve implements Resolve<IAlien> {
  constructor(private service: AlienService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IAlien> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Alien>) => response.ok),
        map((alien: HttpResponse<Alien>) => alien.body)
      );
    }
    return of(new Alien());
  }
}

export const alienRoute: Routes = [
  {
    path: '',
    component: AlienComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Aliens'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: AlienDetailComponent,
    resolve: {
      alien: AlienResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Aliens'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: AlienUpdateComponent,
    resolve: {
      alien: AlienResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Aliens'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: AlienUpdateComponent,
    resolve: {
      alien: AlienResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Aliens'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const alienPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: AlienDeletePopupComponent,
    resolve: {
      alien: AlienResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Aliens'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
