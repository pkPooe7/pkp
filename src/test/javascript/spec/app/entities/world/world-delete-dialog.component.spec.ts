import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReBaseTestModule } from '../../../test.module';
import { WorldDeleteDialogComponent } from 'app/entities/world/world-delete-dialog.component';
import { WorldService } from 'app/entities/world/world.service';

describe('Component Tests', () => {
  describe('World Management Delete Component', () => {
    let comp: WorldDeleteDialogComponent;
    let fixture: ComponentFixture<WorldDeleteDialogComponent>;
    let service: WorldService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [WorldDeleteDialogComponent]
      })
        .overrideTemplate(WorldDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorldDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorldService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
