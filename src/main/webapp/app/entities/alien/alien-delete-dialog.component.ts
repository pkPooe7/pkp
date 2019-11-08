import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAlien } from 'app/shared/model/alien.model';
import { AlienService } from './alien.service';

@Component({
  selector: 'jhi-alien-delete-dialog',
  templateUrl: './alien-delete-dialog.component.html'
})
export class AlienDeleteDialogComponent {
  alien: IAlien;

  constructor(protected alienService: AlienService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.alienService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'alienListModification',
        content: 'Deleted an alien'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-alien-delete-popup',
  template: ''
})
export class AlienDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ alien }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(AlienDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.alien = alien;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/alien', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/alien', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
