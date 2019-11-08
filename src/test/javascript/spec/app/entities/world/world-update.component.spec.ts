import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ReBaseTestModule } from '../../../test.module';
import { WorldUpdateComponent } from 'app/entities/world/world-update.component';
import { WorldService } from 'app/entities/world/world.service';
import { World } from 'app/shared/model/world.model';

describe('Component Tests', () => {
  describe('World Management Update Component', () => {
    let comp: WorldUpdateComponent;
    let fixture: ComponentFixture<WorldUpdateComponent>;
    let service: WorldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [WorldUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(WorldUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorldUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorldService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new World(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new World();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
