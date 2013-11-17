<?php

class RumahsewaController extends Controller {

    /**
     * @var string the default layout for the views. Defaults to '//layouts/column2', meaning
     * using two-column layout. See 'protected/views/layouts/column2.php'.
     */
    public $layout = '//layouts/column2';

    protected function publicActions() {
        return array("index", "create", "update", "delete");
    }

    /**
     * @return array action filters
     */
    public function filters() {
        return array(
            'accessControl', // perform access control for CRUD operations
        );
    }

    /**
     * Specifies the access control rules.
     * This method is used by the 'accessControl' filter.
     * @return array access control rules
     */
    public function accessRules() {
        return array(
            array('allow', // allow all users to perform 'index' and 'view' actions
                'actions' => array('index', 'view', 'create'),
                'users' => array('*'),
            ),
            array('allow', // allow authenticated user to perform 'create' and 'update' actions
                'actions' => array('update'),
                'users' => array('*'),
            ),
            array('allow', // allow admin user to perform 'admin' and 'delete' actions
                'actions' => array('admin', 'delete'),
                'users' => array('*'),
            ),
            array('deny', // deny all users
                'users' => array('*'),
            ),
        );
    }

    /**
     * Displays a particular model.
     * @param integer $id the ID of the model to be displayed
     */
    public function actionView($id) {
        $this->render('view', array(
            'model' => $this->loadModel($id),
        ));
    }

    /**
     * Creates a new model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     */
    public function actionCreate() {
        $model = new Rumahsewa;

        // Uncomment the following line if AJAX validation is needed
        // $this->performAjaxValidation($model);

        if (isset($_POST)) {
            $model->attributes = $_POST;

            $uploads_dir = Yii::app()->basePath . '/..' . '/uploads';
            $uploads_url = Yii::app()->baseUrl . '/uploads';
            $file = $_FILES['foto'];
            $error = $file["error"];
            if ($error == UPLOAD_ERR_OK) {
                $tmp_name = $file["tmp_name"];
                $name = $file["name"];
                move_uploaded_file($tmp_name, "$uploads_dir/$name");
                $model->foto = "$uploads_url/$name";
            }
            if (!$model->save()) {
                print_r($model->getErrors());
                return;
            }
        }

        print_r($model);
    }

    /**
     * Updates a particular model.
     * If update is successful, the browser will be redirected to the 'view' page.
     * @param integer $id the ID of the model to be updated
     */
    public function actionUpdate($id) {
        $model = $this->loadModel($id);

        // Uncomment the following line if AJAX validation is needed
        // $this->performAjaxValidation($model);
        if (isset($_POST)) {
            $model->attributes = $_POST;
            if (!$model->save()) {
                Helper::returnData(array('data' => print_r($model->getErrors(), true)), 500);
                return;
            }
        }

        Helper::returnData(array("data" => $model));
    }

    /**
     * Deletes a particular model.
     * If deletion is successful, the browser will be redirected to the 'admin' page.
     * @param integer $id the ID of the model to be deleted
     */
    public function actionDelete($id) {
        $this->loadModel($id)->delete();

        Helper::returnData(array());
//        // if AJAX request (triggered by deletion via admin grid view), we should not redirect the browser
//        if (!isset($_GET['ajax']))
//            $this->redirect(isset($_POST['returnUrl']) ? $_POST['returnUrl'] : array('admin'));
    }

    /**
     * Lists all models.
     */
    public function actionIndex() {
        $model = Rumahsewa::model()->findAll();
        Helper::returnData(array("data" => $model));
    }

    /**
     * Manages all models.
     */
    public function actionAdmin() {
        $model = new Rumahsewa('search');
        $model->unsetAttributes();  // clear any default values
        if (isset($_GET['Rumahsewa']))
            $model->attributes = $_GET['Rumahsewa'];

        $this->render('admin', array(
            'model' => $model,
        ));
    }

    /**
     * Returns the data model based on the primary key given in the GET variable.
     * If the data model is not found, an HTTP exception will be raised.
     * @param integer $id the ID of the model to be loaded
     * @return Rumahsewa the loaded model
     * @throws CHttpException
     */
    public function loadModel($id) {
        $model = Rumahsewa::model()->findByPk($id);
        if ($model === null)
            throw new CHttpException(404, 'The requested page does not exist.');
        return $model;
    }

    /**
     * Performs the AJAX validation.
     * @param Rumahsewa $model the model to be validated
     */
    protected function performAjaxValidation($model) {
        if (isset($_POST['ajax']) && $_POST['ajax'] === 'rumahsewa-form') {
            echo CActiveForm::validate($model);
            Yii::app()->end();
        }
    }

}
