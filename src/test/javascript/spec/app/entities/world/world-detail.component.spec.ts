import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReBaseTestModule } from '../../../test.module';
import { WorldDetailComponent } from 'app/entities/world/world-detail.component';
import { World } from 'app/shared/model/world.model';

describe('Component Tests', () => {
  describe('World Management Detail Component', () => {
    let comp: WorldDetailComponent;
    let fixture: ComponentFixture<WorldDetailComponent>;
    const route = ({ data: of({ world: new World(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ReBaseTestModule],
        declarations: [WorldDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(WorldDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(WorldDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.world).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
