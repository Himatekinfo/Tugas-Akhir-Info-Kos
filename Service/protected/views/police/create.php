<?php
/* @var $this PoliceController */
/* @var $model Police */

$this->breadcrumbs=array(
	'Polices'=>array('index'),
	'Create',
);

$this->menu=array(
	array('label'=>'List Police', 'url'=>array('index')),
	array('label'=>'Manage Police', 'url'=>array('admin')),
);
?>

<h1>Create Police</h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>