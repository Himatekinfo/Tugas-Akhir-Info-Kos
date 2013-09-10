<?php
/* @var $this IncidentImageController */
/* @var $model IncidentImage */

$this->breadcrumbs=array(
	'Incident Images'=>array('index'),
	$model->Id,
);

$this->menu=array(
	array('label'=>'List IncidentImage', 'url'=>array('index')),
	array('label'=>'Create IncidentImage', 'url'=>array('create')),
	array('label'=>'Update IncidentImage', 'url'=>array('update', 'id'=>$model->Id)),
	array('label'=>'Delete IncidentImage', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->Id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage IncidentImage', 'url'=>array('admin')),
);
?>

<h1>View IncidentImage #<?php echo $model->Id; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'Id',
		'FilePath',
		'IncidentId',
	),
)); ?>
