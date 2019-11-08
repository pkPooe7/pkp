import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IWorld, World } from 'app/shared/model/world.model';
import { WorldService } from './world.service';
import { ISolarSystem } from 'app/shared/model/solar-system.model';
import { SolarSystemService } from 'app/entities/solar-system/solar-system.service';

@Component({
  selector: 'jhi-world-update',
  templateUrl: './world-update.component.html'
})
export class WorldUpdateComponent implements OnInit {
  isSaving: boolean;

  solarsystems: ISolarSystem[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    system: [null, [Validators.required, Validators.maxLength(50)]],
    homeSystem: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected worldService: WorldService,
    protected solarSystemService: SolarSystemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ world }) => {
      this.updateForm(world);
    });
    this.solarSystemService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ISolarSystem[]>) => mayBeOk.ok),
        map((response: HttpResponse<ISolarSystem[]>) => response.body)
      )
      .subscribe((res: ISolarSystem[]) => (this.solarsystems = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(world: IWorld) {
    this.editForm.patchValue({
      id: world.id,
      name: world.name,
      system: world.system,
      homeSystem: world.homeSystem
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const world = this.createFromForm();
    if (world.id !== undefined) {
      this.subscribeToSaveResponse(this.worldService.update(world));
    } else {
      this.subscribeToSaveResponse(this.worldService.create(world));
    }
  }

  private createFromForm(): IWorld {
    return {
      ...new World(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      system: this.editForm.get(['system']).value,
      homeSystem: this.editForm.get(['homeSystem']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorld>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackSolarSystemById(index: number, item: ISolarSystem) {
    return item.id;
  }
}
