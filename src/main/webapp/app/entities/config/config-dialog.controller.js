(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('ConfigDialogController', ConfigDialogController);

    ConfigDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Config'];

    function ConfigDialogController ($scope, $stateParams, $uibModalInstance, entity, Config) {
        var vm = this;
        vm.config = entity;
        vm.load = function(id) {
            Config.get({id : id}, function(result) {
                vm.config = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('musixiseApp:musixiserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.config.id !== null) {
                Config.update(vm.config, onSaveSuccess, onSaveError);
            } else {
                Config.save(vm.config, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
