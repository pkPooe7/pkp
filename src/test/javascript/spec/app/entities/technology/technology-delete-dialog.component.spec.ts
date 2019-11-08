import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReBaseTestModule } from '../../../test.module';
import { TechnologyDeleteDialogComponent } from 'app/entities/technology/technology-delete-dialog.component';
import { TechnologyService } from 'app/entities/technology/technology.service';

describe('Component Tests', () => {
  describe('Technology Management Delete Component', () => {
    let comp: TechnologyDeleteDialogComponent;
    let fixture: ComponentFixture<TechnologyDeleteDialogComponent>;
    let service: TechnologyService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [TechnologyDeleteDialogComponent]
      })
        .overrideTemplate(TechnologyDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TechnologyDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TechnologyService);
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
