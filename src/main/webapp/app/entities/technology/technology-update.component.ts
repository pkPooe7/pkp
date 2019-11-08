import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITechnology, Technology } from 'app/shared/model/technology.model';
import { TechnologyService } from './technology.service';
import { IClassification } from 'app/shared/model/classification.model';
import { ClassificationService } from 'app/entities/classification/classification.service';

@Component({
  selector: 'jhi-technology-update',
  templateUrl: './technology-update.component.html'
})
export class TechnologyUpdateComponent implements OnInit {
  isSaving: boolean;

  classifications: IClassification[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.maxLength(50)]],
    aquired: [null, [Validators.required]],
    speci: [null, [Validators.required, Validators.maxLength(50)]],
    speciesTeches: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected technologyService: TechnologyService,
    protected classificationService: ClassificationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ technology }) => {
      this.updateForm(technology);
    });
    this.classificationService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IClassification[]>) => mayBeOk.ok),
        map((response: HttpResponse<IClassification[]>) => response.body)
      )
      .subscribe((res: IClassification[]) => (this.classifications = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(technology: ITechnology) {
    this.editForm.patchValue({
      id: technology.id,
      name: technology.name,
      aquired: technology.aquired,
      speci: technology.speci,
      speciesTeches: technology.speciesTeches
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const technology = this.createFromForm();
    if (technology.id !== undefined) {
      this.subscribeToSaveResponse(this.technologyService.update(technology));
    } else {
      this.subscribeToSaveResponse(this.technologyService.create(technology));
    }
  }

  private createFromForm(): ITechnology {
    return {
      ...new Technology(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      aquired: this.editForm.get(['aquired']).value,
      speci: this.editForm.get(['speci']).value,
      speciesTeches: this.editForm.get(['speciesTeches']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITechnology>>) {
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

  trackClassificationById(index: number, item: IClassification) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
