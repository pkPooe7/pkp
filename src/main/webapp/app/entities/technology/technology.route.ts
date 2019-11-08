import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Technology } from 'app/shared/model/technology.model';
import { TechnologyService } from './technology.service';
import { TechnologyComponent } from './technology.component';
import { TechnologyDetailComponent } from './technology-detail.component';
import { TechnologyUpdateComponent } from './technology-update.component';
import { TechnologyDeletePopupComponent } from './technology-delete-dialog.component';
import { ITechnology } from 'app/shared/model/technology.model';

@Injectable({ providedIn: 'root' })
export class TechnologyResolve implements Resolve<ITechnology> {
  constructor(private service: TechnologyService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ITechnology> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Technology>) => response.ok),
        map((technology: HttpResponse<Technology>) => technology.body)
      );
    }
    return of(new Technology());
  }
}

export const technologyRoute: Routes = [
  {
    path: '',
    component: TechnologyComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: TechnologyDetailComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: TechnologyUpdateComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: TechnologyUpdateComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const technologyPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: TechnologyDeletePopupComponent,
    resolve: {
      technology: TechnologyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Technologies'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
