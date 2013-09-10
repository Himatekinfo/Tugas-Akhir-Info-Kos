<?php
/* @var $this IncidentController */
/* @var $model Incident */

$this->breadcrumbs=array(
	'Incidents'=>array('index'),
	$model->Id,
);

$this->menu=array(
	array('label'=>'List Incident', 'url'=>array('index')),
	array('label'=>'Create Incident', 'url'=>array('create')),
	array('label'=>'Update Incident', 'url'=>array('update', 'id'=>$model->Id)),
	array('label'=>'Delete Incident', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->Id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage Incident', 'url'=>array('admin')),
);
?>

<h1>View Incident #<?php echo $model->Id; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'Id',
		'IncidentTime',
		'Location',
		'Culprit',
		'Victim',
		'LossValue',
		'Description',
		'Latitude',
		'Longitude',
		'Type',
		'PoliceId',
		'PoliceActivity',
	),
)); ?>
