import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { World } from 'app/shared/model/world.model';
import { WorldService } from './world.service';
import { WorldComponent } from './world.component';
import { WorldDetailComponent } from './world-detail.component';
import { WorldUpdateComponent } from './world-update.component';
import { WorldDeletePopupComponent } from './world-delete-dialog.component';
import { IWorld } from 'app/shared/model/world.model';

@Injectable({ providedIn: 'root' })
export class WorldResolve implements Resolve<IWorld> {
  constructor(private service: WorldService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IWorld> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<World>) => response.ok),
        map((world: HttpResponse<World>) => world.body)
      );
    }
    return of(new World());
  }
}

export const worldRoute: Routes = [
  {
    path: '',
    component: WorldComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Worlds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: WorldDetailComponent,
    resolve: {
      world: WorldResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Worlds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: WorldUpdateComponent,
    resolve: {
      world: WorldResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Worlds'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: WorldUpdateComponent,
    resolve: {
      world: WorldResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Worlds'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const worldPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: WorldDeletePopupComponent,
    resolve: {
      world: WorldResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Worlds'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
