import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorld } from 'app/shared/model/world.model';
import { WorldService } from './world.service';

@Component({
  selector: 'jhi-world-delete-dialog',
  templateUrl: './world-delete-dialog.component.html'
})
export class WorldDeleteDialogComponent {
  world: IWorld;

  constructor(protected worldService: WorldService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.worldService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'worldListModification',
        content: 'Deleted an world'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-world-delete-popup',
  template: ''
})
export class WorldDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ world }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WorldDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.world = world;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/world', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/world', { outlets: { popup: null } }]);
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
