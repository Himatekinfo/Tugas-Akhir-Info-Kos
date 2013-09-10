<?php
/* @var $this PoliceController */
/* @var $model Police */

$this->breadcrumbs=array(
	'Polices'=>array('index'),
	$model->Name=>array('view','id'=>$model->Id),
	'Update',
);

$this->menu=array(
	array('label'=>'List Police', 'url'=>array('index')),
	array('label'=>'Create Police', 'url'=>array('create')),
	array('label'=>'View Police', 'url'=>array('view', 'id'=>$model->Id)),
	array('label'=>'Manage Police', 'url'=>array('admin')),
);
?>

<h1>Update Police <?php echo $model->Id; ?></h1>

<?php $this->renderPartial('_form', array('model'=>$model)); ?>