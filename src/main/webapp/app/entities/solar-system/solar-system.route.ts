import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { SolarSystem } from 'app/shared/model/solar-system.model';
import { SolarSystemService } from './solar-system.service';
import { SolarSystemComponent } from './solar-system.component';
import { SolarSystemDetailComponent } from './solar-system-detail.component';
import { SolarSystemUpdateComponent } from './solar-system-update.component';
import { SolarSystemDeletePopupComponent } from './solar-system-delete-dialog.component';
import { ISolarSystem } from 'app/shared/model/solar-system.model';

@Injectable({ providedIn: 'root' })
export class SolarSystemResolve implements Resolve<ISolarSystem> {
  constructor(private service: SolarSystemService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ISolarSystem> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<SolarSystem>) => response.ok),
        map((solarSystem: HttpResponse<SolarSystem>) => solarSystem.body)
      );
    }
    return of(new SolarSystem());
  }
}

export const solarSystemRoute: Routes = [
  {
    path: '',
    component: SolarSystemComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolarSystems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: SolarSystemDetailComponent,
    resolve: {
      solarSystem: SolarSystemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolarSystems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: SolarSystemUpdateComponent,
    resolve: {
      solarSystem: SolarSystemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolarSystems'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: SolarSystemUpdateComponent,
    resolve: {
      solarSystem: SolarSystemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolarSystems'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const solarSystemPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: SolarSystemDeletePopupComponent,
    resolve: {
      solarSystem: SolarSystemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'SolarSystems'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
