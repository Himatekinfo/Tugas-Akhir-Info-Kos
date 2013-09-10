<?php
/* @var $this IncidentImageController */
/* @var $model IncidentImage */
/* @var $form CActiveForm */
?>

<div class="form">

<?php $form=$this->beginWidget('CActiveForm', array(
	'id'=>'incident-image-form',
	// Please note: When you enable ajax validation, make sure the corresponding
	// controller action is handling ajax validation correctly.
	// There is a call to performAjaxValidation() commented in generated controller code.
	// See class documentation of CActiveForm for details on this.
	'enableAjaxValidation'=>false,
)); ?>

	<p class="note">Fields with <span class="required">*</span> are required.</p>

	<?php echo $form->errorSummary($model); ?>

	<div class="row">
		<?php echo $form->labelEx($model,'FilePath'); ?>
		<?php echo $form->textField($model,'FilePath',array('size'=>60,'maxlength'=>500)); ?>
		<?php echo $form->error($model,'FilePath'); ?>
	</div>

	<div class="row">
		<?php echo $form->labelEx($model,'IncidentId'); ?>
		<?php echo $form->textField($model,'IncidentId',array('size'=>20,'maxlength'=>20)); ?>
		<?php echo $form->error($model,'IncidentId'); ?>
	</div>

	<div class="row buttons">
		<?php echo CHtml::submitButton($model->isNewRecord ? 'Create' : 'Save'); ?>
	</div>

<?php $this->endWidget(); ?>

</div><!-- form -->