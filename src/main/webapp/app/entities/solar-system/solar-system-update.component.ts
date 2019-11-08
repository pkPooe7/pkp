import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { ISolarSystem, SolarSystem } from 'app/shared/model/solar-system.model';
import { SolarSystemService } from './solar-system.service';

@Component({
  selector: 'jhi-solar-system-update',
  templateUrl: './solar-system-update.component.html'
})
export class SolarSystemUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    galaxy: [null, [Validators.required, Validators.maxLength(50)]]
  });

  constructor(protected solarSystemService: SolarSystemService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ solarSystem }) => {
      this.updateForm(solarSystem);
    });
  }

  updateForm(solarSystem: ISolarSystem) {
    this.editForm.patchValue({
      id: solarSystem.id,
      name: solarSystem.name,
      galaxy: solarSystem.galaxy
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const solarSystem = this.createFromForm();
    if (solarSystem.id !== undefined) {
      this.subscribeToSaveResponse(this.solarSystemService.update(solarSystem));
    } else {
      this.subscribeToSaveResponse(this.solarSystemService.create(solarSystem));
    }
  }

  private createFromForm(): ISolarSystem {
    return {
      ...new SolarSystem(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      galaxy: this.editForm.get(['galaxy']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISolarSystem>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
