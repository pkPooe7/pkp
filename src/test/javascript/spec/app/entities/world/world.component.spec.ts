import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReBaseTestModule } from '../../../test.module';
import { WorldComponent } from 'app/entities/world/world.component';
import { WorldService } from 'app/entities/world/world.service';
import { World } from 'app/shared/model/world.model';

describe('Component Tests', () => {
  describe('World Management Component', () => {
    let comp: WorldComponent;
    let fixture: ComponentFixture<WorldComponent>;
    let service: WorldService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [WorldComponent],
        providers: []
      })
        .overrideTemplate(WorldComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(WorldComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(WorldService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new World(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.worlds[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
