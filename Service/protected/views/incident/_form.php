<?php
/* @var $this IncidentController */
/* @var $model Incident */
/* @var $form CActiveForm */
?>

<div class="form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'incident-form',
	// Please note: When you enable ajax validation, make sure the corresponding
	// controller action is handling ajax validation correctly.
	// There is a call to performAjaxValidation() commented in generated controller code.
	// See class documentation of CActiveForm for details on this.
	'enableAjaxValidation'=>false,
)); ?>

	<p class="note">Fields with <span class="required">*</span> are required.</p>

	<?php echo $form->errorSummary($model); ?>

	<div class="row">
		<?php echo $form->labelEx($model,'Id'); ?>
		<?php echo $form->textField($model,'Id',array('size'=>20,'maxlength'=>20)); ?>
		<?php echo $form->error($model,'Id'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'IncidentTime'); ?>
		<?php echo $form->textField($model,'IncidentTime'); ?>
		<?php echo $form->error($model,'IncidentTime'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Location'); ?>
		<?php echo $form->textField($model,'Location',array('size'=>60,'maxlength'=>100)); ?>
		<?php echo $form->error($model,'Location'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Culprit'); ?>
		<?php echo $form->textField($model,'Culprit',array('size'=>60,'maxlength'=>100)); ?>
		<?php echo $form->error($model,'Culprit'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Victim'); ?>
		<?php echo $form->textField($model,'Victim',array('size'=>60,'maxlength'=>100)); ?>
		<?php echo $form->error($model,'Victim'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'LossValue'); ?>
		<?php echo $form->textField($model,'LossValue',array('size'=>50,'maxlength'=>50)); ?>
		<?php echo $form->error($model,'LossValue'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Description'); ?>
		<?php echo $form->textField($model,'Description',array('size'=>60,'maxlength'=>500)); ?>
		<?php echo $form->error($model,'Description'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Latitude'); ?>
		<?php echo $form->textField($model,'Latitude'); ?>
		<?php echo $form->error($model,'Latitude'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Longitude'); ?>
		<?php echo $form->textField($model,'Longitude'); ?>
		<?php echo $form->error($model,'Longitude'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'Type'); ?>
		<?php echo $form->textField($model,'Type',array('size'=>30,'maxlength'=>30)); ?>
		<?php echo $form->error($model,'Type'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'PoliceId'); ?>
		<?php echo $form->textField($model,'PoliceId',array('size'=>20,'maxlength'=>20)); ?>
		<?php echo $form->error($model,'PoliceId'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'PoliceActivity'); ?>
		<?php echo $form->textField($model,'PoliceActivity',array('size'=>10,'maxlength'=>10)); ?>
		<?php echo $form->error($model,'PoliceActivity'); ?>
	</div>

	<div class="row buttons">
		<?php echo CHtml::submitButton($model->isNewRecord ? 'Create' : 'Save'); ?>
	</div>

<?php $this->endWidget(); ?>

</div><!-- form -->