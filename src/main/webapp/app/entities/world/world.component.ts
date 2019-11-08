import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IWorld } from 'app/shared/model/world.model';
import { AccountService } from 'app/core/auth/account.service';
import { WorldService } from './world.service';

@Component({
  selector: 'jhi-world',
  templateUrl: './world.component.html'
})
export class WorldComponent implements OnInit, OnDestroy {
  worlds: IWorld[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected worldService: WorldService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.worldService
      .query()
      .pipe(
        filter((res: HttpResponse<IWorld[]>) => res.ok),
        map((res: HttpResponse<IWorld[]>) => res.body)
      )
      .subscribe((res: IWorld[]) => {
        this.worlds = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInWorlds();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWorld) {
    return item.id;
  }

  registerChangeInWorlds() {
    this.eventSubscriber = this.eventManager.subscribe('worldListModification', response => this.loadAll());
  }
}
