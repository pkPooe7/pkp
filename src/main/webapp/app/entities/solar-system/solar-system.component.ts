import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { ISolarSystem } from 'app/shared/model/solar-system.model';
import { AccountService } from 'app/core/auth/account.service';
import { SolarSystemService } from './solar-system.service';

@Component({
  selector: 'jhi-solar-system',
  templateUrl: './solar-system.component.html'
})
export class SolarSystemComponent implements OnInit, OnDestroy {
  solarSystems: ISolarSystem[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected solarSystemService: SolarSystemService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.solarSystemService
      .query()
      .pipe(
        filter((res: HttpResponse<ISolarSystem[]>) => res.ok),
        map((res: HttpResponse<ISolarSystem[]>) => res.body)
      )
      .subscribe((res: ISolarSystem[]) => {
        this.solarSystems = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInSolarSystems();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ISolarSystem) {
    return item.id;
  }

  registerChangeInSolarSystems() {
    this.eventSubscriber = this.eventManager.subscribe('solarSystemListModification', response => this.loadAll());
  }
}
