<?php
/* @var $this IncidentController */
/* @var $data Incident */
?>

<div class="view">

	<b><?php echo CHtml::encode($data->getAttributeLabel('Id')); ?>:</b>
	<?php echo CHtml::link(CHtml::encode($data->Id), array('view', 'id'=>$data->Id)); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('IncidentTime')); ?>:</b>
	<?php echo CHtml::encode($data->IncidentTime); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('Location')); ?>:</b>
	<?php echo CHtml::encode($data->Location); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('Culprit')); ?>:</b>
	<?php echo CHtml::encode($data->Culprit); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('Victim')); ?>:</b>
	<?php echo CHtml::encode($data->Victim); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('LossValue')); ?>:</b>
	<?php echo CHtml::encode($data->LossValue); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('Description')); ?>:</b>
	<?php echo CHtml::encode($data->Description); ?>
	<br />

	<?php /*
	<b><?php echo CHtml::encode($data->getAttributeLabel('Latitude')); ?>:</b>
	<?php echo CHtml::encode($data->Latitude); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('Longitude')); ?>:</b>
	<?php echo CHtml::encode($data->Longitude); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('Type')); ?>:</b>
	<?php echo CHtml::encode($data->Type); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('PoliceId')); ?>:</b>
	<?php echo CHtml::encode($data->PoliceId); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('PoliceActivity')); ?>:</b>
	<?php echo CHtml::encode($data->PoliceActivity); ?>
	<br />

	*/ ?>

</div>