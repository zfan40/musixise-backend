(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserDialogController', MusixiserDialogController);

    MusixiserDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Musixiser'];

    function MusixiserDialogController ($scope, $stateParams, $uibModalInstance, entity, Musixiser) {
        var vm = this;
        vm.musixiser = entity;
        vm.load = function(id) {
            Musixiser.get({id : id}, function(result) {
                vm.musixiser = result;
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
            if (vm.musixiser.id !== null) {
                Musixiser.update(vm.musixiser, onSaveSuccess, onSaveError);
            } else {
                Musixiser.save(vm.musixiser, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
