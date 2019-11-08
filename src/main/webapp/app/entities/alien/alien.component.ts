import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager } from 'ng-jhipster';

import { IAlien } from 'app/shared/model/alien.model';
import { AccountService } from 'app/core/auth/account.service';
import { AlienService } from './alien.service';

@Component({
  selector: 'jhi-alien',
  templateUrl: './alien.component.html'
})
export class AlienComponent implements OnInit, OnDestroy {
  aliens: IAlien[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(protected alienService: AlienService, protected eventManager: JhiEventManager, protected accountService: AccountService) {}

  loadAll() {
    this.alienService
      .query()
      .pipe(
        filter((res: HttpResponse<IAlien[]>) => res.ok),
        map((res: HttpResponse<IAlien[]>) => res.body)
      )
      .subscribe((res: IAlien[]) => {
        this.aliens = res;
      });
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    this.registerChangeInAliens();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IAlien) {
    return item.id;
  }

  registerChangeInAliens() {
    this.eventSubscriber = this.eventManager.subscribe('alienListModification', response => this.loadAll());
  }
}
