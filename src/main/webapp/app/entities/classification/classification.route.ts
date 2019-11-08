import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Classification } from 'app/shared/model/classification.model';
import { ClassificationService } from './classification.service';
import { ClassificationComponent } from './classification.component';
import { ClassificationDetailComponent } from './classification-detail.component';
import { ClassificationUpdateComponent } from './classification-update.component';
import { ClassificationDeletePopupComponent } from './classification-delete-dialog.component';
import { IClassification } from 'app/shared/model/classification.model';

@Injectable({ providedIn: 'root' })
export class ClassificationResolve implements Resolve<IClassification> {
  constructor(private service: ClassificationService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IClassification> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Classification>) => response.ok),
        map((classification: HttpResponse<Classification>) => classification.body)
      );
    }
    return of(new Classification());
  }
}

export const classificationRoute: Routes = [
  {
    path: '',
    component: ClassificationComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Classifications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ClassificationDetailComponent,
    resolve: {
      classification: ClassificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Classifications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ClassificationUpdateComponent,
    resolve: {
      classification: ClassificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Classifications'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ClassificationUpdateComponent,
    resolve: {
      classification: ClassificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Classifications'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const classificationPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ClassificationDeletePopupComponent,
    resolve: {
      classification: ClassificationResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Classifications'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
