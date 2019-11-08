import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ReBaseTestModule } from '../../../test.module';
import { AlienComponent } from 'app/entities/alien/alien.component';
import { AlienService } from 'app/entities/alien/alien.service';
import { Alien } from 'app/shared/model/alien.model';

describe('Component Tests', () => {
  describe('Alien Management Component', () => {
    let comp: AlienComponent;
    let fixture: ComponentFixture<AlienComponent>;
    let service: AlienService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [AlienComponent],
        providers: []
      })
        .overrideTemplate(AlienComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlienComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(AlienService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Alien(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.aliens[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
