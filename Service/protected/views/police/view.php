<?php
/* @var $this PoliceController */
/* @var $model Police */

$this->breadcrumbs=array(
	'Polices'=>array('index'),
	$model->Name,
);

$this->menu=array(
	array('label'=>'List Police', 'url'=>array('index')),
	array('label'=>'Create Police', 'url'=>array('create')),
	array('label'=>'Update Police', 'url'=>array('update', 'id'=>$model->Id)),
	array('label'=>'Delete Police', 'url'=>'#', 'linkOptions'=>array('submit'=>array('delete','id'=>$model->Id),'confirm'=>'Are you sure you want to delete this item?')),
	array('label'=>'Manage Police', 'url'=>array('admin')),
);
?>

<h1>View Police #<?php echo $model->Id; ?></h1>

<?php $this->widget('zii.widgets.CDetailView', array(
	'data'=>$model,
	'attributes'=>array(
		'Id',
		'Name',
	),
)); ?>
