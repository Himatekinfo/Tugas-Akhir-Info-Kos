<?php
/* @var $this IncidentImageController */
/* @var $data IncidentImage */
?>

<div class="view">

	<b><?php echo CHtml::encode($data->getAttributeLabel('Id')); ?>:</b>
	<?php echo CHtml::link(CHtml::encode($data->Id), array('view', 'id'=>$data->Id)); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('FilePath')); ?>:</b>
	<?php echo CHtml::encode($data->FilePath); ?>
	<br />

	<b><?php echo CHtml::encode($data->getAttributeLabel('IncidentId')); ?>:</b>
	<?php echo CHtml::encode($data->IncidentId); ?>
	<br />


</div>