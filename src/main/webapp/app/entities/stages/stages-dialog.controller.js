(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('StagesDialogController', StagesDialogController);

    StagesDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Stages'];

    function StagesDialogController ($scope, $stateParams, $uibModalInstance, entity, Stages) {
        var vm = this;
        vm.stages = entity;
        vm.load = function(id) {
            Stages.get({id : id}, function(result) {
                vm.stages = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:stagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.stages.id !== null) {
                Stages.update(vm.stages, onSaveSuccess, onSaveError);
            } else {
                Stages.save(vm.stages, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.createtime = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
